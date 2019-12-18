using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Net.Http;
using WeatherApp.Model;
using Xamarin.Forms;
using System.Net;
using Newtonsoft.Json;
using System.Threading.Tasks;

namespace WeatherApp.ViewModel
{
    public class CitiesViewModel
    {
        public string[] PortugueseCapitals =
        {
            "Aveiro",
            "Beja",
            "Braga",
            "Bragança",
            "Castelo Branco",
            "Coimbra",
            "Évora",
            "Faro",
            "Guarda",
            "Leiria",
            "Lisboa",
            "Portalegre",
            "Porto",
            "Santarém",
            "Setúbal",
            "Viana do Castelo",
            "Vila Real",
            "Viseu"
        };

        public ObservableCollection<CityViewModel> Cities { get; set; } = new ObservableCollection<CityViewModel>();

        public List<CityViewModel> AllCities { get; set; }

        public CitiesViewModel()
        {
            AllCities = GetAllCities();
        }

        public async void AddCity(CityViewModel city)
        {
            await UpdateCityWeather(city);
            Cities.Add(city);
        }

        public Command<CityViewModel> Remove_City
        {
            get{
                return new Command<CityViewModel>(city =>
                {
                    Cities.Remove(city);
                });
            }
        }

        public void updateWeathers()
        {
            foreach (CityViewModel c in Cities)
                UpdateCityWeather(c);
        }

        public async Task UpdateCityWeather(CityViewModel cityVM)
        {
            City city = cityVM.getCity();

            using (HttpClient client = new HttpClient())
                try
                {
                    HttpResponseMessage response = await client.GetAsync(API.getWeatherURL(city));
                    if (response.StatusCode == HttpStatusCode.OK)
                    {
                        Weather apiWeather = JsonConvert.DeserializeObject<Weather>(
                            await response.Content.ReadAsStringAsync()
                        );

                        city.UpdateWeather(apiWeather);
                    }
                }
                catch (Exception ex)
                {
                    Console.Error.Write(ex.StackTrace);
                }
            return;
        }

        public List<CityViewModel> GetAllCities()
        {
            List<CityViewModel> cities = new List<CityViewModel>();

            foreach (string capital in PortugueseCapitals)
            {
                cities.Add(new City(capital).getViewModel());
            }

            return cities;
        }
    }
}
