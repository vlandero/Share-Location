namespace Backend.Models.DTOs.ManyToManyDTO
{
    public class ManyToManyDTO : IManyToManyDTO
    {
        public Guid Id1 { get; set; }
        public Guid Id2 { get; set; }
    }
}
