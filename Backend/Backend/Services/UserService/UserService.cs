using Backend.Helpers.JwtHelpers;
using Backend.Models;
using Backend.Models.Connected;
using Backend.Models.DTOs.UserLoginRequestDTO;
using Backend.Models.DTOs.UserLoginResponseDTO;
using Backend.Models.DTOs.UserRegisterRequestDTO;
using Backend.Models.DTOs.UserToBeStoredDTO;
using Backend.Models.Rejected;
using Backend.Models.User;
using Microsoft.EntityFrameworkCore;
using BCryptNet = BCrypt.Net.BCrypt;

namespace Backend.Services.UserService
{
    public class UserService : IUserService
    {
        private readonly ShareLocationContext _context;
        private readonly DbSet<User> _table;
        private readonly IJWTHelpers _jwtHelpers;
        public UserService(ShareLocationContext context, IJWTHelpers jwtHelpers)
        {
            _context = context;
            _table = _context.Set<User>();
            _jwtHelpers = jwtHelpers;
        }

        public List<UserToBeStoredDTO> Shuffle(List<UserToBeStoredDTO> list)
        {
            Random rng = new Random();
            int n = list.Count;
            while (n > 1)
            {
                n--;
                int k = rng.Next(n + 1);
                (list[n], list[k]) = (list[k], list[n]);
            }
            return list;
        }

        public User GetById(Guid id)
        {
            return _table.Find(id);
        }
        public User GetByUsername(string username)
        {
            return _table.FirstOrDefault(u => u.Username == username);
        }
        public UserToBeStoredDTO Registration(UserRegisterRequestDTO userDTO)
        {
            var user = new User(userDTO);
            _context.Add(user);
            _context.SaveChanges();
            return new UserToBeStoredDTO(user);
        }

        public UserToBeStoredDTO ModifyUser(UserToBeStoredDTO userDTO)
        {
            var user = GetById(userDTO.Id) ?? throw new Exception("Invalid user id");
            user.About = userDTO.About;
            user.Email = userDTO.Email;
            user.Username = userDTO.Username;
            user.Photos = userDTO.Photos;
            user.Location = userDTO.Location;
            user.Name = userDTO.Name;
            user.Phone = userDTO.Phone;
            _context.Update(user);
            _context.SaveChanges();
            return userDTO;
        }
        public List<UserToBeStoredDTO> GetAllUsers()
        {
            var users = _table.ToList();
            var usersDTO = new List<UserToBeStoredDTO>();
            foreach (var user in users)
            {
                usersDTO.Add(new UserToBeStoredDTO(user));
            }
            return usersDTO;
        }
        public List<UserToBeStoredDTO> GetConnected(Guid userId)
        {
            var user = GetById(userId) ?? throw new Exception("Invalid user id");
            var connectedUsers = _context.Connecteds
                .Include(c => c.User1)
                .Include(c => c.User2)
                .Where(a => a.Id1 == user.Id || a.Id2 == user.Id)
                .ToList();
            var connectedUserIds = connectedUsers.Select(c => c.Id1 == userId ? c.Id2 : c.Id1);
            var connectedUsersList = _context.Users
                .Where(u => connectedUserIds.Contains(u.Id))
                .Select(u => new UserToBeStoredDTO
                {
                    Id = u.Id,
                    Age = u.Age,
                    Username = u.Username,
                    Email = u.Email,
                    Name = u.Name,
                    Phone = u.Phone,
                    Photos = u.Photos,
                    About = u.About,
                    Location = u.Location
                })
                .ToList();
            return connectedUsersList;
        }
        public List<UserToBeStoredDTO> GetRejected(Guid userId)
        {
            var user = GetById(userId) ?? throw new Exception("Invalid user id");
            var rejectedUsers = _context.Rejecteds
                .Include(c => c.User1)
                .Include(c => c.User2)
                .Where(a => a.Id1 == user.Id || a.Id2 == user.Id)
                .ToList();
            var rejectedUserIds = rejectedUsers.Select(c => c.Id1 == userId ? c.Id2 : c.Id1);
            var rejectedUsersList = _context.Users
                .Where(u => rejectedUserIds.Contains(u.Id))
                .Select(u => new UserToBeStoredDTO
                {
                    Id = u.Id,
                    Age = u.Age,
                    Username = u.Username,
                    Email = u.Email,
                    Name = u.Name,
                    Phone = u.Phone,
                    Photos = u.Photos,
                    About = u.About,
                    Location = u.Location
                })
                .ToList();
            return rejectedUsersList;
        }
        public List<UserToBeStoredDTO> GetUsersForFeed(Guid userId)
        {
            var allUsers = GetAllUsers();
            var rejectedUsers = GetRejected(userId)
                .ToDictionary(u => u.Id);
            var connectedUsers = GetConnected(userId)
                .ToDictionary(u => u.Id);
            var users = allUsers.Where(u => u.Id != userId
                && !rejectedUsers.ContainsKey(u.Id)
                && !connectedUsers.ContainsKey(u.Id))
                .ToList();
            return Shuffle(users);
        }
        public void Connect(Guid userId1, Guid userId2)
        {
            var user1 = GetById(userId1) ?? throw new Exception("Invalid user id");
            var user2 = GetById(userId2) ?? throw new Exception("Invalid user id");
            var con = new Connected(user1, user2);
            _context.Add(con);
            _context.SaveChanges();
        }
        public void DeleteConnection(Guid userId1, Guid userId2)
        {
            var user1 = GetById(userId1) ?? throw new Exception("Invalid user id");
            var user2 = GetById(userId2) ?? throw new Exception("Invalid user id");
            var con = _context.Connecteds.FirstOrDefault(a => (a.Id1 == user1.Id && a.Id2 == user2.Id) || (a.Id1 == user2.Id && a.Id2 == user1.Id));
            if (con != null)
            {
                _context.Remove(con);
                _context.SaveChanges();
            }
        }
        public void Reject(Guid userId1, Guid userId2)
        {
            var user1 = GetById(userId1) ?? throw new Exception("Invalid user id");
            var user2 = GetById(userId2) ?? throw new Exception("Invalid user id");
            var rej = new Rejected(user1, user2);
            _context.Add(rej);
            _context.SaveChanges();
        }
        public void DeleteRejection(Guid userId1, Guid userId2)
        {
            var user1 = GetById(userId1) ?? throw new Exception("Invalid user id");
            var user2 = GetById(userId2) ?? throw new Exception("Invalid user id");
            var rej = _context.Rejecteds.FirstOrDefault(a => (a.Id1 == user1.Id && a.Id2 == user2.Id) || (a.Id1 == user2.Id && a.Id2 == user1.Id));
            if (rej != null)
            {
                _context.Remove(rej);
                _context.SaveChanges();
            }
        }
        public UserLoginResponseDTO Login(UserLoginRequestDTO request)
        {
            var user = GetByUsername(request.Username);
            if (user == null || !BCryptNet.Verify(request.Password, user.Password))
            {
                throw new Exception("Invalid email or password");
            }
            var token = _jwtHelpers.GenerateJwtToken(new UserToBeStoredDTO(user));
            return new UserLoginResponseDTO
            {
                Token = token,
            };
        }

        public void DeleteAll()
        {
            var users = _table.ToList();
            foreach (var user in users)
            {
                _context.Remove(user);
            }
            _context.SaveChanges();
        }
    }
}
