using System;
using WeatherApp.ViewModel;

namespace WeatherApp.Model
{
    public class MainStats: DynamicViewModel
    {
        private string _CurrentTempDiff;
        public string CurrentTempDiff { get => _CurrentTempDiff; set => SetProperty(ref _CurrentTempDiff, value); }

        private string _CurrentPressure;
        public string CurrentPressure { get => _CurrentPressure; set => SetProperty(ref _CurrentPressure, value); }

        private string _CurrentPreciptitation;
        public string CurrentPreciptitation { get => _CurrentPreciptitation; set => SetProperty(ref _CurrentPreciptitation, value); }

        private string _CurrentWind;
        public string CurrentWind { get => _CurrentWind; set => SetProperty(ref _CurrentWind, value); }

        private string _CurrentHumidity;
        public string CurrentHumidity { get => _CurrentHumidity; set => SetProperty(ref _CurrentHumidity, value); }

        public MainStats(Weather weather)
        {
            CurrentTempDiff = Math.Round(weather.main.temp_max).ToString() +
                "-" + Math.Round(weather.main.temp_min).ToString() + "ºC";
            CurrentPressure = weather.main.pressure.ToString() + "hpa";
            //city.CurrentPreciptitation = apiWeather;
            CurrentWind = weather.wind.speed.ToString() + "m/s";
            CurrentHumidity = weather.main.humidity.ToString() + "%";
        }
    }
}
