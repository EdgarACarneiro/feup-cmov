using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Http;
using Newtonsoft.Json;
using WeatherApp.Model;
using Xamarin.Essentials;

namespace WeatherApp.ViewModel
{
    public class CityViewModel
    {
        public List<City> Cities { get; set; }
        public List<City> AllCities { get; set; }

        private string endpoint = "https://api.openweathermap.org/data/2.5/",
            key = "appid=744c7d488901b071cef81d5efeb9a5b3";

        public CityViewModel()
        {
            Cities = new City().GetCities();
            AllCities = new City().GetAllCities();
        }

        public void updateWeathers()
        {
            foreach (City c in Cities)
                UpdateCityWeather(c);
        }

        public async void UpdateCityWeather(City city)
        {
            if (Connectivity.NetworkAccess != NetworkAccess.Internet)
            {
                String url = String.Format(
                    "{0}weather?q={1},pt&units=metric&{2}",
                    endpoint,
                    city.Name,
                    key
                );
                Console.WriteLine(" :::::::: YAYAYAYAYAYAYAYAYAYYAYAYAYAYAYA");

                using (HttpClient client = new HttpClient())
                    try
                    {
                        HttpResponseMessage response = await client.GetAsync(url);
                        if (response.StatusCode == HttpStatusCode.OK)
                        {
                            string content = await response.Content.ReadAsStringAsync();
                            city.CurrentWeather = JsonConvert.DeserializeObject<Weather>(content);
                            city.CurrentTemp = city.CurrentWeather.main.temp.ToString() + "ºC";
                            Console.WriteLine(" :::::::: YAYAYAYAYAYAYAYAYAYYAYAYAYAYAYA");
                        }
                    }
                    catch (Exception ex)
                    {
                        Console.Error.Write(ex.StackTrace);
                        Console.WriteLine(" :::::::: YAYAYAYAYAYAYAYAYAYYAYAYAYAYAYA");
                    }
            }
        }
    }
}
