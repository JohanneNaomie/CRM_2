using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using NewApp.Models;


namespace NewApp.Controllers;

public class ConfigAlerteController : Controller
{
    private readonly ILogger<ConfigAlerteController> _logger;

    public ConfigAlerteController(ILogger<ConfigAlerteController> logger)
    {
        _logger = logger;
    }

    public IActionResult Insert()
    {
        return View();
    }

    [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
    public IActionResult Error()
    {
        return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
    }
}
    

