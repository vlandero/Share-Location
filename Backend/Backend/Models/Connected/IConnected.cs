namespace Backend.Models.Connected
{
    public interface IConnected
    {
        Guid Id1 { get; set; }
        Guid Id2 { get; set; }
        User.User User1 { get; set; }
        User.User User2 { get; set; }
    }
}
