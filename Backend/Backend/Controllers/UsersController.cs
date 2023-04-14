using Backend.Models.DTOs.UserRegisterRequestDTO;
using Backend.Models.DTOs.UserToBeStoredDTO;
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
        [HttpPost("modify")]
        public IActionResult Modify(UserToBeStoredDTO user)
        {
            try
            {
                var modifiedUser = _userService.ModifyUser(user);
                return Ok(modifiedUser);
            }
            catch(Exception ex)
            {
                if(ex.InnerException == null)
                {
                    return BadRequest(ex.Message);
                }
                return BadRequest(ex.InnerException.Message);

            }
        }

    }
}
