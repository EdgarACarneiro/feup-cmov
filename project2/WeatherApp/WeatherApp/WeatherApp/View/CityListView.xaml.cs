using System;
using System.Collections.ObjectModel;
using WeatherApp.View;
using WeatherApp.ViewModel;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace WeatherApp
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class CityListView : ContentPage
    {
        public ObservableCollection<string> Items { get; set; }
        CityViewModel vm;

        public CityListView()
        {
            vm = new CityViewModel();
            vm.updateWeathers();
            cityList.ItemsSource = vm.Cities;
            cityPicker.ItemsSource = vm.AllCities;


            InitializeComponent();
        }

        async void Handle_ItemTapped(object sender, ItemTappedEventArgs e)
        {
            if (e.Item == null)
                return;

            //await DisplayAlert("Item Tapped", "An item was tapped.", "OK");
            await Navigation.PushModalAsync(new NavigationPage(new CityView()));

            //Deselect Item
            ((ListView)sender).SelectedItem = null;
        }

        private void Button_Clicked(object sender, EventArgs e)
        {
            // TRigger Forecast request
        }


    }
}
