using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using WeatherApp.Model;

namespace WeatherApp.ViewModel
{
    public class CityViewModel
    {
        public List<City> Cities { get; set; }
        public List<City> AllCities { get; set; }
        public WeatherAPI api;
        
        public CityViewModel()
        {
            Cities = new City().GetCities();
            AllCities = new City().GetAllCities();

            api = new WeatherAPI();
        }

        public async void GetWeathers()
        {
            // Making API Requests
            Dictionary<City, Task<HttpResponseMessage>> tasks =
                new Dictionary<City, Task<HttpResponseMessage>>();

            foreach (City c in Cities)
                tasks.Add(c, api.FetchWeather(c));

            // Retriving responses
            foreach (City c in tasks.Keys)
                c.CurrentWeather = api.parseWeather(await tasks[c]);
        }
    }
}
