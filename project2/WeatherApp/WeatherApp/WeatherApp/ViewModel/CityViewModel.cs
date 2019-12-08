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

        private string endpoint = "http://api.openweathermap.org/data/2.5/",
            key = "appid=744c7d488901b071cef81d5efeb9a5b3";


        public CityViewModel()
        {
            AllCities = new City().GetAllCities();
        }

        public void updateWeathers()
        {
            foreach (City c in Cities)
                UpdateCityWeather(c);
        }

        public async void UpdateCityWeather(City city)
        {   
            String url = String.Format(
                "{0}weather?q={1},pt&units=metric&{2}",
                endpoint,
                city.Name,
                key
            );

            using (HttpClient client = new HttpClient())
                try
                {
                    HttpResponseMessage response = await client.GetAsync(url);
                    if (response.StatusCode == HttpStatusCode.OK)
                    {
                        Weather apiWeather = JsonConvert.DeserializeObject<Weather>(
                            await response.Content.ReadAsStringAsync()
                        );

                        // Updating City
                        city.CurrentTemp = apiWeather.main.temp.ToString() + "ºC";
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
