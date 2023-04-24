namespace Backend.Models.DTOs.UserLoginRequestDTO
{
    public class UserLoginRequestDTO : IUserLoginRequestDTO
    {
        public string Username { get; set; }
        public string Password { get; set; }
    }
}
