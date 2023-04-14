using Backend.Models;
using Backend.Models.DTOs.UserRegisterRequestDTO;
using Backend.Models.DTOs.UserToBeStoredDTO;
using Backend.Models.User;
using Microsoft.EntityFrameworkCore;
using Npgsql;

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
        public string Test(string t)
        {
            return t + " Test";
        }

        public User GetById(Guid id)
        {
            return _table.Find(id);
        }

        public UserToBeStoredDTO Registration(UserRegisterRequestDTO userDTO)
        {
            var user = new User(userDTO);
            _table.Add(user);
            _context.SaveChanges();
            return new UserToBeStoredDTO(user);
            
        }
    }
}
