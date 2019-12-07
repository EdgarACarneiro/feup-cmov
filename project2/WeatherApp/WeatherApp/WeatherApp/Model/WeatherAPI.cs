using System;
using System.ComponentModel;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace WeatherApp.Model
{
    public class WeatherAPI: INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;

        private string endpoint = "https://api.openweathermap.org/data/2.5/",
            key = "appid=744c7d488901b071cef81d5efeb9a5b3";

        private string status, result;

        private Weather weather;

        public Task<HttpResponseMessage> FetchWeather(City city)
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
                    return client.GetAsync(url);
                }
                catch (Exception ex)
                {
                    Console.Error.Write(ex.StackTrace);
                    return null;
                }
        }

        public Weather parseWeather(HttpResponseMessage message)
        {
            Console.Write(message);
            if (message.StatusCode == HttpStatusCode.OK)
                return JsonConvert.DeserializeObject<Weather>(message.Content.ToString());
            return new Weather();
        }
    }
}
