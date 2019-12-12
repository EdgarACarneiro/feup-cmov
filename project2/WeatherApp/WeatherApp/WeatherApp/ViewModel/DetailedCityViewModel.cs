using System;
using System.Net;
using System.Net.Http;
using Newtonsoft.Json;
using WeatherApp.Model;
using WeatherApp.View;

namespace WeatherApp.ViewModel
{
    public class DetailedCityViewModel
    {
        public City city;
        public CityView view;

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
                        Forecast apiForecast = JsonConvert.DeserializeObject<Forecast>(
                            await response.Content.ReadAsStringAsync()
                        );

                        city.DetailedUpdateModel(apiForecast);
                        if (view != null)
                            view.SetGraphIcons();
                    }
                }
                catch (Exception ex)
                {
                    Console.Error.Write(ex.StackTrace);
                }
        }
    }
}
