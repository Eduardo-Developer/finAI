package com.edudev.finai.data.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
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
public final class AuthRepositoryImpl_Factory implements Factory<AuthRepositoryImpl> {
  private final Provider<FirebaseAuth> authProvider;

  private final Provider<FirebaseDatabase> databaseProvider;

  public AuthRepositoryImpl_Factory(Provider<FirebaseAuth> authProvider,
      Provider<FirebaseDatabase> databaseProvider) {
    this.authProvider = authProvider;
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AuthRepositoryImpl get() {
    return newInstance(authProvider.get(), databaseProvider.get());
  }

  public static AuthRepositoryImpl_Factory create(Provider<FirebaseAuth> authProvider,
      Provider<FirebaseDatabase> databaseProvider) {
    return new AuthRepositoryImpl_Factory(authProvider, databaseProvider);
  }

  public static AuthRepositoryImpl newInstance(FirebaseAuth auth, FirebaseDatabase database) {
    return new AuthRepositoryImpl(auth, database);
  }
}
