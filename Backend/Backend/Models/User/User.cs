namespace Backend.Models.User
{
    public class User : IUser
    {
        public Guid Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        public string Email { get; set; }
        public string Name { get; set; }
        public string Phone { get; set; }
        public List<string> Photos { get; set; }
        public string About { get; set; }
        public string Location { get; set; }
    }
}
