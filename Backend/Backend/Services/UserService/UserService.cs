using Backend.Models;
using Backend.Models.Connected;
using Backend.Models.DTOs.UserRegisterRequestDTO;
using Backend.Models.DTOs.UserToBeStoredDTO;
using Backend.Models.Rejected;
using Backend.Models.User;
using Microsoft.EntityFrameworkCore;

namespace Backend.Services.UserService
{
    public class UserService : IUserService
    {
        private readonly ShareLocationContext _context;
        private readonly DbSet<User> _table;
        public UserService(ShareLocationContext context)
        {
            _context = context;
            _table = _context.Set<User>();
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
        public List<Connected> GetConnected(Guid guid)
        {
            var user = GetById(guid) ?? throw new Exception("Invalid user id");
            var connected = _context.Connecteds.Where(a => a.Id1 == user.Id || a.Id2 == user.Id).ToList();
            return connected;
        }
        public List<Rejected> GetRejected(Guid guid)
        {
            var user = GetById(guid) ?? throw new Exception("Invalid user id");
            var rejected = _context.Rejecteds.Where(a => a.Id1 == user.Id || a.Id2 == user.Id).ToList();
            return rejected;
        }
        public List<UserToBeStoredDTO> GetUsersForFeed(Guid userId)
        {
            var allUsers = GetAllUsers();
            var rejectedUsers = GetRejected(userId);

            var users = _context.Users
                .Where(u => !u.Rejecteds.Any(r => r.Id1 == userId || r.Id2 == userId))
                .Where(u => !u.Connecteds.Any(r => r.Id1 == userId || r.Id2 == userId))
                .Select(u => new UserToBeStoredDTO(u))
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
        public void Reject(Guid userId1, Guid userId2)
        {
            var user1 = GetById(userId1) ?? throw new Exception("Invalid user id");
            var user2 = GetById(userId2) ?? throw new Exception("Invalid user id");
            var rej = new Rejected(user1, user2);
            _context.Add(rej);
            _context.SaveChanges();
        }
    }
}
