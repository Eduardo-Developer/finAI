package com.edudev.finai.data.repository;

import androidx.datastore.core.DataStore;
import androidx.datastore.preferences.core.Preferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class ThemeRepositoryImpl_Factory implements Factory<ThemeRepositoryImpl> {
  private final Provider<DataStore<Preferences>> dataStoreProvider;

  public ThemeRepositoryImpl_Factory(Provider<DataStore<Preferences>> dataStoreProvider) {
    this.dataStoreProvider = dataStoreProvider;
  }

  @Override
  public ThemeRepositoryImpl get() {
    return newInstance(dataStoreProvider.get());
  }

  public static ThemeRepositoryImpl_Factory create(
      Provider<DataStore<Preferences>> dataStoreProvider) {
    return new ThemeRepositoryImpl_Factory(dataStoreProvider);
  }

  public static ThemeRepositoryImpl newInstance(DataStore<Preferences> dataStore) {
    return new ThemeRepositoryImpl(dataStore);
  }
}
