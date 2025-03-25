using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using NewApp.Models;


namespace NewApp.Controllers;

public class HomeController : Controller
{
    private readonly ILogger<HomeController> _logger;

    public HomeController(ILogger<HomeController> logger)
    {
        _logger = logger;
    }

    public IActionResult Index()
    {
        return View();
    }

    public IActionResult Dashboard()
    {
        var chartData1 = new Dictionary<string, int>
        {
            { "Category A", 40 },
            { "Category B", 30 },
            { "Category C", 20 },
            { "Category D", 5 },
            { "Category E", 5 }
        };

        var chartData2 = new Dictionary<string, int>
        {
            { "Product X", 50 },
            { "Product Y", 25 },
            { "Product Z", 25 }
        };

        var chartData3 = new Dictionary<string, int>
        {
            { "Service 1", 60 },
            { "Service 2", 20 },
            { "Service 3", 20 }
        };

        ViewBag.ChartData1 = chartData1;
        ViewBag.ChartData2 = chartData2;
        ViewBag.ChartData3 = chartData3;

        return View();
    }

    public IActionResult Privacy()
    {
        return View();
    }

    [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
    public IActionResult Error()
    {
        return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
    }
}
    

