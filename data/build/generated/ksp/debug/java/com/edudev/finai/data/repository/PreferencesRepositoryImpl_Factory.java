package com.edudev.finai.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class PreferencesRepositoryImpl_Factory implements Factory<PreferencesRepositoryImpl> {
  private final Provider<Context> contextProvider;

  private final Provider<SharedPreferences> encryptedPrefsProvider;

  public PreferencesRepositoryImpl_Factory(Provider<Context> contextProvider,
      Provider<SharedPreferences> encryptedPrefsProvider) {
    this.contextProvider = contextProvider;
    this.encryptedPrefsProvider = encryptedPrefsProvider;
  }

  @Override
  public PreferencesRepositoryImpl get() {
    return newInstance(contextProvider.get(), encryptedPrefsProvider.get());
  }

  public static PreferencesRepositoryImpl_Factory create(Provider<Context> contextProvider,
      Provider<SharedPreferences> encryptedPrefsProvider) {
    return new PreferencesRepositoryImpl_Factory(contextProvider, encryptedPrefsProvider);
  }

  public static PreferencesRepositoryImpl newInstance(Context context,
      SharedPreferences encryptedPrefs) {
    return new PreferencesRepositoryImpl(context, encryptedPrefs);
  }
}
