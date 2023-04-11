using Backend.Models.User;
using Microsoft.EntityFrameworkCore;

namespace Backend.Models
{
    public class ShareLocationContext : DbContext
    {
        public DbSet<User.User> Users { get; set; }
        public ShareLocationContext(DbContextOptions<ShareLocationContext> options) : base(options) { }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<User.User>(e => e.HasIndex(i => i.Email).IsUnique());
            modelBuilder.Entity<User.User>(e => e.HasIndex(i => i.Username).IsUnique());

            base.OnModelCreating(modelBuilder);
        }
    }
}
