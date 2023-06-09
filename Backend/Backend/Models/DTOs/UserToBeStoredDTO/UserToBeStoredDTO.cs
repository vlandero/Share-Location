﻿using System.Net.Cache;
using System.Text.Json.Serialization;

namespace Backend.Models.DTOs.UserToBeStoredDTO
{
    public class UserToBeStoredDTO : IUserToBeStoredDTO
    {
        public Guid Id { get; set; }
        public string Username { get; set; }
        public string Email { get; set; }
        public string Age { get; set; }
        public string Name { get; set; }
        public string Phone { get; set; }
        public List<string> Photos { get; set; }
        public string About { get; set; }
        public string Location { get; set; }
        [JsonConstructor]
        public UserToBeStoredDTO()
        {
        }

        public UserToBeStoredDTO(User.User u)
        {
            Id = u.Id;
            Age = u.Age;
            Username = u.Username;
            Email = u.Email;
            Name = u.Name;
            Phone = u.Phone;
            Photos = u.Photos;
            About = u.About;
            Location = u.Location;
        }
    }
}
