using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using NewApp.Services;  // Assuming your DashboardService is in the Services folder
using System.Threading.Tasks;

namespace NewApp.Controllers
{
    public class DashboardController : Controller
    {
        private readonly DashboardService _dashboardService;
        private readonly ILogger<DashboardController> _logger;

        // Constructor to inject the DashboardService and Logger
        public DashboardController(DashboardService dashboardService, ILogger<DashboardController> logger)
        {
            _dashboardService = dashboardService;
            _logger = logger;
        }

        // Action method for the Dashboard page
        public async Task<IActionResult> Dashboard()
        {
            // Fetch the data for the charts
            var chartData1 = await _dashboardService.GetTop5Customers();
            var chartData2 = await _dashboardService.GetLeadByStatus();
            var chartData3 = await _dashboardService.GetTicketByStatus();

            // Pass the data to the view using ViewBag
            ViewBag.ChartData1 = chartData1;
            ViewBag.ChartData2 = chartData2;
            ViewBag.ChartData3 = chartData3;

            return View();
        }

        public async Task<IActionResult> DetailsTicket()
        {
            return View();
        }
        public async Task<IActionResult> DetailsLead()
        {
            return View();
        }
        public async Task<IActionResult> DetailsCustomerDepense()
        {
            return View();
        }
    }
}
