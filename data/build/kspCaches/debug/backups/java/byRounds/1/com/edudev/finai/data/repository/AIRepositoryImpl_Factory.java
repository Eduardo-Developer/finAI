package com.edudev.finai.data.repository;

import com.google.ai.client.generativeai.GenerativeModel;
import com.squareup.moshi.Moshi;
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
public final class AIRepositoryImpl_Factory implements Factory<AIRepositoryImpl> {
  private final Provider<GenerativeModel> generativeModelProvider;

  private final Provider<Moshi> moshiProvider;

  public AIRepositoryImpl_Factory(Provider<GenerativeModel> generativeModelProvider,
      Provider<Moshi> moshiProvider) {
    this.generativeModelProvider = generativeModelProvider;
    this.moshiProvider = moshiProvider;
  }

  @Override
  public AIRepositoryImpl get() {
    return newInstance(generativeModelProvider.get(), moshiProvider.get());
  }

  public static AIRepositoryImpl_Factory create(Provider<GenerativeModel> generativeModelProvider,
      Provider<Moshi> moshiProvider) {
    return new AIRepositoryImpl_Factory(generativeModelProvider, moshiProvider);
  }

  public static AIRepositoryImpl newInstance(GenerativeModel generativeModel, Moshi moshi) {
    return new AIRepositoryImpl(generativeModel, moshi);
  }
}
