using System.Text.Json.Serialization;

namespace Backend.Models.Rejected
{
    public class Rejected : IRejected
    {
        public Guid Id1 { get; set; }
        public Guid Id2 { get; set; }
        public User.User User1 { get; set; }
        public User.User User2 { get; set; }

        [JsonConstructor]
        public Rejected() { }

        public Rejected(User.User user1, User.User user2)
        {
            Id1 = user1.Id;
            Id2 = user2.Id;
            User1 = user1;
            User2 = user2;
        }
    }
}
