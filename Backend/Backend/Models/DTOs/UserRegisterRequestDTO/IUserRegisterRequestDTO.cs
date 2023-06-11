namespace Backend.Models.DTOs.UserRegisterRequestDTO
{
    public interface IUserRegisterRequestDTO
    {
        string Username { get; set; }
        string Age { get; set; }
        string Password { get; set; }
        string Email { get; set; }
        string Name { get; set; }
        string Phone { get; set; }
        List<string> Photos { get; set; }
        string About { get; set; }
        string Location { get; set; }
    }
}
