using System.Text.Json.Serialization;

namespace Backend.Models.DTOs.UserRegisterRequestDTO
{
    public class UserRegisterRequestDTO
    {
        public string Username { get; set; }
        public string Password { get; set; }
        public string Email { get; set; }
        public string Name { get; set; }
        public string Phone { get; set; }
        public List<string> Photos { get; set; }
        public string About { get; set; }
        public string Location { get; set; }

        [JsonConstructor]
        public UserRegisterRequestDTO()
        {
        }
    }
}
