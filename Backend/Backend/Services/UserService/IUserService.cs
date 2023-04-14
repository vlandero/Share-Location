using Backend.Models.DTOs.UserRegisterRequestDTO;
using Backend.Models.DTOs.UserToBeStoredDTO;
using Backend.Models.User;

namespace Backend.Services.UserService
{
    public interface IUserService
    {
        User GetById(Guid id);
        UserToBeStoredDTO Registration(UserRegisterRequestDTO user);
        UserToBeStoredDTO ModifyUser(UserToBeStoredDTO user);
    }
}
