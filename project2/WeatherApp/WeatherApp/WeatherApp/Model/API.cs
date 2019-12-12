using System;
namespace WeatherApp.Model
{
    public class API
    {
        public static string Endpoint =
            "http://api.openweathermap.org/data/2.5/";
        public static string Key =
            "appid=744c7d488901b071cef81d5efeb9a5b3";
        public static string IconStart =
            "https://openweathermap.org/img/wn/";

        public static string getWeatherURL(City city)
        {
            return String.Format(
                "{0}weather?q={1},pt&units=metric&{2}",
                Endpoint,
                city.Name,
                Key
            );
        }

        public static string getForecastURL(City city)
        {
            return String.Format(
                "{0}forecast?q={1},pt&units=metric&{2}",
                Endpoint,
                city.Name,
                Key
            );
        }

        public static Uri getIconUri(string icon)
        {
            return new Uri(String.Format(
                "{0}{1}@2x.png",
                IconStart,
                icon
            ));
        }
    }
}
