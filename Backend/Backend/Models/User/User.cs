using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;
using Backend.Models.DTOs.UserRegisterRequestDTO;
using BCryptNet = BCrypt.Net.BCrypt;
using System.Text.Json.Serialization;

namespace Backend.Models.User
{
    public class User : IUser
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid Id { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }
        public string Email { get; set; }
        public string Name { get; set; }
        public string Phone { get; set; }
        public List<string> Photos { get; set; }
        public string About { get; set; }
        public string Location { get; set; }
        public string Token { get; set; }

        [JsonConstructor]
        public User() { }

        public User(UserRegisterRequestDTO u)
        {
            Username = u.Username;
            Password = BCryptNet.HashPassword(u.Password);
            Name = u.Name;
            Email = u.Email;
            Phone = u.Phone;
            Photos = u.Photos;
            About = u.About;
            Location = u.Location;
            Token = "";
        }
    }
}
