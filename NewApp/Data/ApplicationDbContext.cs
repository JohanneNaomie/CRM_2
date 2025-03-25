using Microsoft.EntityFrameworkCore;

namespace NewApp.Data
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options) : base(options) { }

        
        
        // Configuring relationships and constraints
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            
        }
    }
}
