using Backend.Models.DTOs.UserRegisterRequestDTO;
using Backend.Services.UserService;
using Microsoft.AspNetCore.Mvc;

namespace Backend.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {
        private readonly IUserService _userService;
        public UsersController(IUserService userService)
        {
            _userService = userService;
        }
        [HttpGet("test/{ts}")]
        public IActionResult TestGet(string ts)
        {
            var k = _userService.Test(ts);
            return Ok(k);
        }
        [HttpPost("register")]
        public IActionResult Register(UserRegisterRequestDTO u)
        {
            try
            { 
                var res = _userService.Registration(u);
                return Ok(res);
            }
            catch (Microsoft.EntityFrameworkCore.DbUpdateException ex)
            {
                return BadRequest(ex.InnerException.Message);
            }
}

    }
}
