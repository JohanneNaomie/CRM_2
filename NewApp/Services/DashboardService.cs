using System.Net.Http;
using System.Text.Json;
using System.Threading.Tasks;
using System.Collections.Generic;

namespace NewApp.Services;

public class DashboardService
{
    private readonly HttpClient _httpClient;

    public DashboardService(HttpClient httpClient)
    {
        _httpClient = httpClient;
    }

    public async Task<Dictionary<string, object>> GetTop5Customers()
    {
        
        var response = await _httpClient.GetAsync("http://localhost:8080/api/customer/top5");
        response.EnsureSuccessStatusCode();
        
        var content = await response.Content.ReadAsStringAsync();
        Console.WriteLine("Response content top 5 : " + content);  // ✅ Debug: afficher la réponse

        var top5Customers = JsonSerializer.Deserialize<List<Dictionary<string, object>>>(content);

        // Assuming you want to map this data to a dictionary
        var result = new Dictionary<string, object>();
        foreach (var customer in top5Customers)
        {
            result.Add(customer["customerId"].ToString(), customer["totalDepense"]);
        }

        return result;
    }

    public async Task<Dictionary<string, object>> GetLeadByStatus()
    {
        var response = await _httpClient.GetAsync("http://localhost:8080/api/lead/leadByStatus");
        response.EnsureSuccessStatusCode();

        var content = await response.Content.ReadAsStringAsync();
        var leadByStatus = JsonSerializer.Deserialize<Dictionary<string, object>>(content);

        return leadByStatus;
    }

    public async Task<Dictionary<string, object>> GetTicketByStatus()
    {
        var response = await _httpClient.GetAsync("http://localhost:8080/api/ticketByStatus");
        response.EnsureSuccessStatusCode();

        var content = await response.Content.ReadAsStringAsync();
        var ticketByStatus = JsonSerializer.Deserialize<Dictionary<string, object>>(content);

        return ticketByStatus;
    }
    public async Task<Dictionary<string, int>> GetTop5Customerstest()
    {
        // Simulate data fetching
        return await Task.FromResult(new Dictionary<string, int>
        {
            { "Customer 1", 150 },
            { "Customer 2", 120 },
            { "Customer 3", 90 },
            { "Customer 4", 75 },
            { "Customer 5", 60 }
        });
    }

    public async Task<Dictionary<string, int>> GetLeadByStatustest()
    {
        return await Task.FromResult(new Dictionary<string, int>
        {
            { "Lead 1", 20 },
            { "Lead 2", 30 },
            { "Lead 3", 50 }
        });
    }

    public async Task<Dictionary<string, int>> GetTicketByStatustest()
    {
        return await Task.FromResult(new Dictionary<string, int>
        {
            { "Open", 25 },
            { "Closed", 75 }
        });
    }
}

