import SwiftUI
import Shared

struct ContentView: View {
    private let appModule = AppModule()
    private let userModule = UserModule()
    private let middlewareModule = MiddlewareModule()
    private let signUpModule = SignUpModule()
    private let splashModule = SplashModule()
    
    private var signUpScreen: SignUpScreen {
        SignUpScreen(
            signUpUseCase: signUpModule.signUpUseCase,
            loginUseCase: signUpModule.loginUseCase
        )
    }
    
    private var splashScreen: SplashScreen {
        SplashScreen(
            getCurrentUserOnInitUseCase: splashModule.getCurrentUserOnInitUseCase
        )
    }
    
    private var userScreen: UserScreen {
        UserScreen(
            getCurrentUserUseCase: userModule.getCurrentUserUseCase,
            updateUserUseCase: userModule.updateUserUseCase,
            updatePasswordUseCase: userModule.updatePasswordUseCase,
            deleteUserUseCase: userModule.deleteUserUseCase,
            logoutUseCase: userModule.logoutUseCase
        )
    }
    
    private var routeListingScreen: RoutesListingScreen {
        RoutesListingScreen(
            getAllRoutesUseCase: middlewareModule.getAllRoutesUseCase
        )
    }
    
    private var routeDetailsScreen: RouteDetailsScreen {
        RouteDetailsScreen(
            requestMappedRouteUseCase: middlewareModule.requestMappedRouteUseCase,
            saveRouteInHistoryUseCase: middlewareModule.saveRouteInHistoryUseCase
        )
    }
    
    private var fillPreConfigsScreen: FillPreConfigsScreen {
        FillPreConfigsScreen()
    }
    
    private var previewScreen: PreviewScreen {
        PreviewScreen(
            requestPreviewUseCase: middlewareModule.requestPreviewUseCase
        )
    }
    
    private var reviewScreen: ReviewScreen {
        ReviewScreen(
            createNewRouteUseCase: middlewareModule.createNewRouteUseCase
        )
    }
    
    private var originalRouteScreen: OriginalRouteScreen {
        OriginalRouteScreen(
            testOriginalRouteUseCase: middlewareModule.testOriginalRouteUseCase
        )
    }
    
    private var fillRouteFieldsScreen: FillRouteFieldsScreen {
        FillRouteFieldsScreen()
    }
    
    @State private var navigationPath = NavigationPath()
    
    var body: some View {
        NavigationStack(path: $navigationPath) {
            splashScreen
                .navigationDestination(for: AppScreen.self) { screen in
                    switch screen {
                    case .splash:
                        splashScreen
                    case .signUp:
                        signUpScreen
                    case .routeListing:
                        routeListingScreen
                    case .routeDetails:
                        routeDetailsScreen
                    case .fillRouteFields:
                        fillRouteFieldsScreen
                    case .fillPreConfigs:
                        fillPreConfigsScreen
                    case .preview:
                        previewScreen
                    case .originalRoute:
                        originalRouteScreen
                    case .review:
                        reviewScreen
                    case .user:
                        userScreen
                    }
                }
        }
        .environment(\.navigate) { screen in
            navigationPath.append(screen)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}
