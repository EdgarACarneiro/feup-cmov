using System;
using System.ComponentModel;
using System.Net;
using System.Net.Http;
using Newtonsoft.Json;

namespace WeatherApp.Model
{
    public class WeatherAPI: INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;

        private String endpoint = "https://api.openweathermap.org/data/2.5/",
            key = "appid=744c7d488901b071cef81d5efeb9a5b3";

        private string status, result;

        private Weather weather;

        public WeatherAPI()
        {}

        public async void fetchWeather(City city)
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
                    HttpResponseMessage message = await client.GetAsync(url);
                    if (message.StatusCode == HttpStatusCode.OK)
                    {
                        weather = JsonConvert.DeserializeObject<Weather>(await message.Content.ReadAsStringAsync());
                        Console.Write(weather);
                    }
                }
                catch (Exception ex)
                {
                    Console.Error.Write(ex.StackTrace);
                    weather = null;
                }
        }

        public Weather GetWeather()
        {
            return weather;
        }
    }
}
