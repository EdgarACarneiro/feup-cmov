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
    public class CitiesViewModel
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

        public CitiesViewModel()
        {
            AllCities = GetAllCities();
        }

        public void updateWeathers()
        {
            foreach (City c in Cities)
                UpdateCityWeather(c);
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

                        city.UpdateModel(apiWeather);
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

        public List<City> GetAllCities()
        {
            List<City> cities = new List<City>()
            {
                new City()
                {
                    Name="Aveiro"
                },
                new City()
                {
                    Name="Beja"
                },
                new City()
                {
                    Name="Braga"
                },
                new City()
                {
                    Name="Bragança"
                },
                new City()
                {
                    Name="Castelo Branco"
                },
                new City()
                {
                    Name="Coimbra"
                },
                new City()
                {
                    Name="Évora"
                },
                new City()
                {
                    Name="Faro"
                },
                new City()
                {
                    Name="Guarda"
                },
                new City()
                {
                    Name="Leiria"
                },
                new City()
                {
                    Name="Lisboa"
                },
                new City()
                {
                    Name="Portalegre"
                },
                new City()
                {
                    Name="Porto"
                },
                new City()
                {
                    Name="Santarém"
                },
                new City()
                {
                    Name="Setúbal"
                },
                new City()
                {
                    Name="Viana do Castelo"
                },
                new City()
                {
                    Name="Vila Real"
                },
                new City()
                {
                    Name="Viseu"
                }
            };
            return cities;
        }
    }
}
