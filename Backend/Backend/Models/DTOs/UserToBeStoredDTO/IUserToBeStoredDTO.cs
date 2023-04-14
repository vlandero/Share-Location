namespace Backend.Models.DTOs.UserToBeStoredDTO
{
    public interface IUserToBeStoredDTO
    {
        Guid Id { get; set; }
        string Username { get; set; }
        string Email { get; set; }
        string Name { get; set; }
        string Phone { get; set; }
        List<string> Photos { get; set; }
        string About { get; set; }
        string Location { get; set; }

    }
}
