using System;
using System.Net;
using System.Net.Http;
using Newtonsoft.Json;
using WeatherApp.Model;

namespace WeatherApp.ViewModel
{
    public class DetailedCityViewModel
    {
        public City city;

        public DetailedCityViewModel(City city)
        {
            this.city = city;
        }

        public async void getCityDetails()
        {
            using (HttpClient client = new HttpClient())
                try
                {
                    HttpResponseMessage response = await client.GetAsync(API.getForecastURL(city));
                    if (response.StatusCode == HttpStatusCode.OK)
                    {
                        Weather apiWeather = JsonConvert.DeserializeObject<Weather>(
                            await response.Content.ReadAsStringAsync()
                        );

                        // Updating Details
                        city.CurrentTemp = apiWeather.main.temp.ToString() + "ºC";
                    }
                }
                catch (Exception ex)
                {
                    Console.Error.Write(ex.StackTrace);
                }
        }
    }
}
