using System;
using System.ComponentModel;
using System.Net;
using System.Net.Http;
using System.Runtime.CompilerServices;

namespace WeatherApp.Model
{
    public class WeatherAPI: INotifyPropertyChanged
    {
        public event PropertyChangedEventHandler PropertyChanged;

        private String endpoint = "https://api.openweathermap.org/data/2.5/";
        private String key = "appid=744c7d488901b071cef81d5efeb9a5b3";

        private string status, result;

        protected void OnPropertyChanged([CallerMemberName] string propertyName = null)
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        protected bool SetProperty<T>(ref T storage, T value, [CallerMemberName] string propertyName = null)
        {
            if (Object.Equals(storage, value))
                return false;
            storage = value;
            OnPropertyChanged(propertyName);
            return true;
        }

        public string Status
        {
            get { return status; }
            set { SetProperty(ref status, value); }
        }
        public string Result
        {
            get { return result; }
            set { SetProperty(ref result, value); }
        }

        private string Extract(string input)
        {
            // JSON Extraction goes here
            return "";
        }


        public WeatherAPI()
        {
        }

        public async void getWeather(City city)
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
                    Status = message.StatusCode.ToString();
                    if (message.StatusCode == HttpStatusCode.OK)
                        Result = Extract(await message.Content.ReadAsStringAsync());
                }
                catch (Exception ex)
                {
                    Result = ex.Message;
                }
        }
    }
}
