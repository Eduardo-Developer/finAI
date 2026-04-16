package com.edudev.finai.di;

import com.google.ai.client.generativeai.GenerativeModel;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class NetworkModule_ProvideGenerativeModelFactory implements Factory<GenerativeModel> {
  @Override
  public GenerativeModel get() {
    return provideGenerativeModel();
  }

  public static NetworkModule_ProvideGenerativeModelFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static GenerativeModel provideGenerativeModel() {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideGenerativeModel());
  }

  private static final class InstanceHolder {
    private static final NetworkModule_ProvideGenerativeModelFactory INSTANCE = new NetworkModule_ProvideGenerativeModelFactory();
  }
}
