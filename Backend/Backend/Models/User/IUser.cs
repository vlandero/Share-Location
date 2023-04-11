namespace Backend.Models.User
{
    public interface IUser
    {
        Guid Id { get; set; }
        string Username { get; set; }
        string Password { get; set; }
        string Email { get; set; }
        string Name { get; set; }
        string Phone { get; set; }
        List<string> Photos { get; set; }
        string About { get; set; }
        string Location { get; set; }
    }
}
