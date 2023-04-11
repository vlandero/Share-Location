using Backend.Helpers.JwtHelpers;
using Backend.Models.User;
using Backend.Services.UserService;
using static Microsoft.EntityFrameworkCore.DbLoggerCategory.Database;

namespace Backend.Helpers
{
    public static class ServiceExtensions
    {
        public static IServiceCollection AddServices(this IServiceCollection services)
        {
            services.AddTransient<IUserService, UserService>();
            return services;
        }

        public static IServiceCollection AddUtils(this IServiceCollection services)
        {
            services.AddScoped<IJWTHelpers, JWTHelpers>();

            return services;
        }
    }
}
