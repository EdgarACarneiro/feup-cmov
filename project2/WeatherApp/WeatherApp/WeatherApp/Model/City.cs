using System;
using System.Collections.Generic;
using WeatherApp.ViewModel;
using Xamarin.Forms;

namespace WeatherApp.Model
{
    public class ForecastPair
    {
        public string Hour { get; set; }
        public MainStats Stats { get; set; }

        public ForecastPair(string h, MainStats s)
        {
            Hour = h;
            Stats = s;
        }
    }

    public class City : DynamicViewModel
    {
        public static readonly int NUM_SELECTED_FORECASTS = 8;
        public static readonly int HOURS_MINS_STRING_SIZE = 5;

        public string Name { get; set; }

        private string _Description;
        public string Description { get => _Description; set => SetProperty(ref _Description, value); }

        public string CountryCode { get; set; } // TODO - Delete

        public string CurrentTime { get; set; } //TODO - Delete

        private string _CurrentTemp;
        public string CurrentTemp { get => _CurrentTemp; set => SetProperty(ref _CurrentTemp, value); }

        public MainStats _CurrentStats;
        public MainStats CurrentStats { get => _CurrentStats; set => SetProperty(ref _CurrentStats, value); }

        public List<ForecastPair> _HourlyStats;
        public List<ForecastPair> HourlyStats { get => _HourlyStats; set => SetProperty(ref _HourlyStats, value); }

        public string[] _Hours = { "--" };
        public string[] Hours { get => _Hours; set => SetProperty(ref _Hours, value); }

        public float[] _Temps = { 0 };
        public float[] Temps { get => _Temps; set => SetProperty(ref _Temps, value); }

        public ImageSource[] _Icons = { };
        public ImageSource[] Icons { get => _Icons; set => SetProperty(ref _Icons, value); }

        public void UpdateModel(Weather weather)
        {
            Description = weather.weather[0].description;
            CurrentTemp = weather.main.temp.ToString() + "ºC";
            CurrentStats = new MainStats(weather);
        }

        public void DetailedUpdateModel(Forecast forecast)
        {
            HourlyStats = new List<ForecastPair>();
            Hours = new string[NUM_SELECTED_FORECASTS];
            Temps = new float[NUM_SELECTED_FORECASTS];
            Icons = new ImageSource[NUM_SELECTED_FORECASTS];

            for (int i = 0; i < forecast.list.Count && i < NUM_SELECTED_FORECASTS; ++i)
            {
                Entry temp = forecast.list[i];
                string time = temp.dt_txt.Split(' ')[1].Remove(HOURS_MINS_STRING_SIZE);
                MainStats stats = new MainStats(temp);

                HourlyStats.Add(new ForecastPair(time, stats));
                Hours.SetValue(time + "h", i);
                Temps.SetValue((float) Math.Round(temp.main.temp, 1), i);
                Icons.SetValue(stats.Icon, i);
            }
        }

        public List<City> GetAllCities()
        {
            List<City> cities = new List<City>()
            {
                new City()
                {
                    Name="Aveiro",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Beja",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Braga",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Bragança",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Castelo Branco",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Coimbra",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Évora",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Faro",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Guarda",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Leiria",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Lisboa",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Portalegre",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Porto",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Santarém",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Setúbal",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Viana do Castelo",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Vila Real",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                },
                new City()
                {
                    Name="Viseu",
                    CountryCode="PT",
                    CurrentTime="16:47",
                    CurrentTemp="--ºC"
                }
            };
            return cities;
        }
    }
}
