using Backend.Models.User;

namespace Backend.Services.UserService
{
    public interface IUserService
    {
        string Test(string t);
        User GetByIdPlaceholder(Guid id);
    }
}
