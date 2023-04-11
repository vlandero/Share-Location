using Backend.Models.User;

namespace Backend.Services.UserService
{
    public class UserService : IUserService
    {
        public string Test(string t)
        {
            return t + " Test";
        }

        public User GetByIdPlaceholder(Guid id)
        {
            return new User
            { 
                About = "",
                Email = "",
                Id = id,
                Location = "",
                Name = "Test",
                Password = "Password",
                Phone = "",
                Photos = new List<string> {}
            };
        }
    }
}
