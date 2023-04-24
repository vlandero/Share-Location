namespace Backend.Models.Rejected
{
    public class IRejected
    {
        Guid Id1 { get; set; }
        Guid Id2 { get; set; }
        User.User User1 { get; set; }
        User.User User2 { get; set; }
    }
}
