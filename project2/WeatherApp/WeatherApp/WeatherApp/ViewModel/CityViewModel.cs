using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using WeatherApp.Model;
using Xamarin.Forms;

namespace WeatherApp.ViewModel
{
    public class CityViewModel
    {
        public ObservableCollection<City> Cities { get; set; } = new ObservableCollection<City>();
        public List<City> AllCities { get; set; }
        public WeatherAPI api;
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

        public void AddCity(City city)
        {
            Cities.Add(city);
        }
    }
}
