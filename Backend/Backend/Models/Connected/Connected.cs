using System.Text.Json.Serialization;

namespace Backend.Models.Connected
{
    public class Connected : IConnected
    {
        public Guid Id1 { get; set; }
        public Guid Id2 { get; set; }
        public User.User User1 { get; set; }
        public User.User User2 { get; set; }

        [JsonConstructor]
        public Connected() { }
        public Connected(User.User user1, User.User user2)
        {
            Id1 = user1.Id;
            Id2 = user2.Id;
            User1 = user1;
            User2 = user2;
        }
    }
}
