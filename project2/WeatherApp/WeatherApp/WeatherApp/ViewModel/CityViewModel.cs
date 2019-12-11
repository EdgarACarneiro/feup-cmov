using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Net.Http;
using WeatherApp.Model;
using Xamarin.Forms;
using System.Net;
using Newtonsoft.Json;

namespace WeatherApp.ViewModel
{
    public class CityViewModel
    {
        public ObservableCollection<City> Cities { get; set; } = new ObservableCollection<City>();
        public List<City> AllCities { get; set; }

        public Command<City> Remove_City
        {
            get{
                return new Command<City>(city =>
                {
                    Cities.Remove(city);
                });
            }
        }

        public CityViewModel()
        {
            AllCities = new City().GetAllCities();
        }

        public void updateWeathers()
        {
            foreach (City c in Cities)
                UpdateCityWeather(c);
        }

        private void updateModel(City city, Weather apiWeather)
        {
            city.Description = apiWeather.weather[0].description;

            // Temperatures
            city.CurrentTemp = apiWeather.main.temp.ToString() + "ºC";
            city.CurrentTempDiff = Math.Round(apiWeather.main.temp_max).ToString() +
                "-" + Math.Round(apiWeather.main.temp_min).ToString() + "ºC";

            // Other Stats
            city.CurrentPressure = apiWeather.main.pressure.ToString() + "hpa";
            //city.CurrentPreciptitation = apiWeather;
            city.CurrentWind = apiWeather.wind.speed.ToString() + "m/s";
            city.CurrentHumidity = apiWeather.main.humidity.ToString() + "%";
        }

        public async void UpdateCityWeather(City city)
        {   
            using (HttpClient client = new HttpClient())
                try
                {
                    HttpResponseMessage response = await client.GetAsync(API.getWeatherURL(city));
                    if (response.StatusCode == HttpStatusCode.OK)
                    {
                        Weather apiWeather = JsonConvert.DeserializeObject<Weather>(
                            await response.Content.ReadAsStringAsync()
                        );

                        updateModel(city, apiWeather);
                    }
                }
                catch (Exception ex)
                {
                    Console.Error.Write(ex.StackTrace);
                }
            
        }

        public void AddCity(City city)
        {
            Cities.Add(city);
            UpdateCityWeather(city);
        }
    }
}
