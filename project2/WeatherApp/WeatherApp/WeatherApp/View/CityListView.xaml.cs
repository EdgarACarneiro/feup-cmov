﻿using System;
using WeatherApp.Model;
using WeatherApp.View;
using WeatherApp.ViewModel;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityListView : ContentPage
    {
        CityViewModel vm;

        public CityListView()
        {
            vm = new CityViewModel();
            vm.updateWeathers();
            BindingContext = vm;

            InitializeComponent();
            cityPicker.ItemsSource = vm.AllCities;
        }

        async void Handle_ItemTapped(object sender, ItemTappedEventArgs e)
        {
            if (e.Item == null)
                return;

            await Navigation.PushModalAsync(new NavigationPage(
                new CityView(new DetailedCityViewModel(vm.Cities[e.ItemIndex]))
            ));

            //Deselect Item
            ((ListView)sender).SelectedItem = null;
        }

        private void Remove_City(object sender, EventArgs e)
        {
            var button = sender as Button;
            var city = button?.BindingContext as City;

            var vm = BindingContext as CityViewModel;
            vm?.Remove_City.Execute(city);
        }

        async private void Add_City(object sender, EventArgs e)
        {
            City pickedCity = (City) cityPicker.SelectedItem;
            int index = vm.Cities.IndexOf(pickedCity);
            if (index >= 0)
            {
                await DisplayAlert("Duplicate City", "This city is already in your favorites.", "OK");
            } else
            {
                vm.AddCity(pickedCity);
            }
        }
    }
}
