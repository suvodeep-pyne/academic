import gym


class Agent:

    def __init__(self, env):
        self.action_size = env.action_space.n

    def next_action(self, obs):
        pole_angle = obs[2]
        action = 0 if pole_angle < 0 else 1

        return action


def main():
    env_name = 'CartPole-v1'
    env = gym.make(env_name)
    print('Observation Space', env.observation_space)
    print('Action Space', env.action_space)

    agent = Agent(env)
    obs = env.reset()

    done = False
    while not done:
        action = agent.next_action(obs)
        obs, reward, done, info = env.step(action)
        env.render()


if __name__ == '__main__':
    main()
