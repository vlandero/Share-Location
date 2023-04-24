namespace Backend.Models.DTOs.UserLoginRequestDTO
{
    public interface IUserLoginRequestDTO
    {
        string Username { get; set; }
        string Password { get; set; }
    }
}
