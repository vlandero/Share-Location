using Microsoft.EntityFrameworkCore;

namespace Backend.Models
{
    public class ShareLocationContext : DbContext
    {
        public DbSet<User.User> Users { get; set; }
        public DbSet<Connected.Connected> Connecteds { get; set; }
        public DbSet<Rejected.Rejected> Rejecteds { get; set; }
        public ShareLocationContext(DbContextOptions<ShareLocationContext> options) : base(options) { }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<User.User>(e => e.HasIndex(i => i.Email).IsUnique());
            modelBuilder.Entity<User.User>(e => e.HasIndex(i => i.Username).IsUnique());

            modelBuilder.Entity<Connected.Connected>().HasKey(a => new { a.Id1, a.Id2 });
            modelBuilder.Entity<Rejected.Rejected>().HasKey(a => new { a.Id1, a.Id2 });

            base.OnModelCreating(modelBuilder);
        }
    }
}
