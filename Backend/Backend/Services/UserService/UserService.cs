using Backend.Models;
using Backend.Models.DTOs.UserRegisterRequestDTO;
using Backend.Models.DTOs.UserToBeStoredDTO;
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

        public User? GetById(Guid id)
        {
            return _table.Find(id);
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
    }
}
