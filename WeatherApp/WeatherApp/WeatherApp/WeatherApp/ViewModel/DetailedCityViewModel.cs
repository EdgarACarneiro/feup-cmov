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
        public CityViewModel cityVM;

        public CityView view;

        public DetailedCityViewModel(CityViewModel cityVM)
        {
            this.cityVM = cityVM;
        }

        public async void getCityDetails()
        {
            City city = cityVM.getCity();

            using (HttpClient client = new HttpClient())
                try
                {
                    HttpResponseMessage response = await client.GetAsync(API.getForecastURL(city));
                    if (response.StatusCode == HttpStatusCode.OK)
                    {
                        Forecast apiForecast = JsonConvert.DeserializeObject<Forecast>(
                            await response.Content.ReadAsStringAsync()
                        );

                        city.UpdateForecast(apiForecast);
                        if (view != null)
                            view.UpdateChart(); // Updating chart since it does not work with binds
                    }
                }
                catch (Exception ex)
                {
                    Console.Error.Write(ex.StackTrace);
                }
        }
    }
}
