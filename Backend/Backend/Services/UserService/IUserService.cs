using Backend.Models.DTOs.UserLoginRequestDTO;
using Backend.Models.DTOs.UserLoginResponseDTO;
using Backend.Models.DTOs.UserRegisterRequestDTO;
using Backend.Models.DTOs.UserToBeStoredDTO;
using Backend.Models.User;

namespace Backend.Services.UserService
{
    public interface IUserService
    {
        User GetById(Guid id);
        User GetByUsername(string username);
        List<UserToBeStoredDTO> Shuffle(List<UserToBeStoredDTO> list);
        UserToBeStoredDTO Registration(UserRegisterRequestDTO user);
        UserToBeStoredDTO ModifyUser(UserToBeStoredDTO user);
        List<UserToBeStoredDTO> GetAllUsers();
        List<UserToBeStoredDTO> GetUsersForFeed(Guid userId);
        void Connect(Guid userId1, Guid userId2);
        void DeleteConnection(Guid userId1, Guid userId2);
        void Reject(Guid userId1, Guid userId2);
        void DeleteRejection(Guid userId1, Guid userId2);
        UserLoginResponseDTO Login(UserLoginRequestDTO user);
        void DeleteAll();

    }
}
